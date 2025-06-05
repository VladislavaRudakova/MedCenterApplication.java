package com.medCenter.medCenter.service.impl;


import com.medCenter.medCenter.dto.PersonalJobDto;
import com.medCenter.medCenter.dto.ScheduleDto;
import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.exception.ScheduleExistException;
import com.medCenter.medCenter.exception.ScheduleNotFoundException;
import com.medCenter.medCenter.model.entity.Days;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.Schedule;
import com.medCenter.medCenter.model.repository.ScheduleRepository;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.ScheduleService;
import com.medCenter.medCenter.service.ServiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PersonalJobService personalJobService;
    private final ServiceService serviceService;


    public ScheduleDto scheduleForPersonalToDto(Schedule schedule) {


        ScheduleDto scheduleDto = ScheduleDto.builder()
                .id(schedule.getId())
                .date(schedule.getDate().toLocalDate())
                .weekDay(schedule.getDate().toLocalDate().getDayOfWeek().toString())
                .build();
        if (schedule.getPersonalJob() != null) {
            PersonalJobDto personalJobDto = personalJobService.personalJobToDto(schedule.getPersonalJob());
            scheduleDto.setPersonalJob(personalJobDto);
        }
        if (schedule.getService() != null) {
            ServiceDto serviceDto = serviceService.findByIdDto(schedule.getService().getId());
            scheduleDto.setService(serviceDto);
        }

        if (schedule.getStartTime() != null) {
            scheduleDto.setStartTime(schedule.getStartTime().toLocalTime());
        }

        if (schedule.getEndTime() != null) {
            scheduleDto.setEndTime(schedule.getEndTime().toLocalTime());
        }
        if (schedule.getDayOff() != null) {
            scheduleDto.setDayOff(schedule.getDayOff());
        }
        if (schedule.getState() != null) {
            scheduleDto.setState(schedule.getState());
        }

        return scheduleDto;
    }


    public Schedule dtoToSchedule(ScheduleDto scheduleDto) {

        Schedule schedule = Schedule.builder()
                .date(Date.valueOf(scheduleDto.getDate()))
                .build();

        if (scheduleDto.getStartTime() != null) {
            schedule.setStartTime(Time.valueOf(scheduleDto.getStartTime()));
        }
        if (scheduleDto.getEndTime() != null) {
            schedule.setEndTime(Time.valueOf(scheduleDto.getEndTime()));
        }
        if (scheduleDto.getPersonalJob() != null) {
            PersonalJob personalJob = personalJobService.findById(scheduleDto.getPersonalJob().getId());
            schedule.setPersonalJob(personalJob);
        }
        if (scheduleDto.getService() != null) {
            com.medCenter.medCenter.model.entity.Service service = serviceService.findById(scheduleDto.getService().getId());
            schedule.setService(service);
        }
        if (scheduleDto.getDayOff() != null) {
            schedule.setDayOff(scheduleDto.getDayOff());
        }
        return schedule;
    }


    @Override
    public List<ScheduleDto> findByUserId(Integer userId, String state) throws ScheduleNotFoundException {
        List<Schedule> scheduleList = scheduleRepository.findByUserId(userId, state);
        if (scheduleList.isEmpty()) {
            throw new ScheduleNotFoundException();
        }
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleDto scheduleDto = scheduleForPersonalToDto(schedule);
            scheduleDtoList.add(scheduleDto);
        }

        return scheduleDtoList;
    }

    @Override
    public ScheduleDto findById(Integer id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return scheduleForPersonalToDto(schedule);
    }

    @Override
    public List<ScheduleDto> findByPersonalId(String personalJobId) throws ScheduleNotFoundException {
        List<Schedule> scheduleList = scheduleRepository.findByPersonalId(Integer.valueOf(personalJobId), "open");
        if (scheduleList.isEmpty()) throw new ScheduleNotFoundException();
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleDto scheduleDto = scheduleForPersonalToDto(schedule);
            scheduleDtoList.add(scheduleDto);
        }
        return scheduleDtoList;
    }

    //------------------------------------------------------------------------create
    @Transactional
    @Override
    public void setScheduleForDay(ScheduleDto scheduleDto) throws ScheduleExistException {
        List<ScheduleDto> scheduleList = new ArrayList<>();

        Schedule schedule = Schedule.builder()
                .date(Date.valueOf(scheduleDto.getDate()))
                .startTime(Time.valueOf(scheduleDto.getStartTime()))
                .endTime(Time.valueOf(scheduleDto.getEndTime()))
                .dayOff(scheduleDto.getDayOff())
                .state(scheduleDto.getState())
                .build();
        if (scheduleDto.getPersonalJob() != null) {
            PersonalJob personalJob = personalJobService.findById(scheduleDto.getPersonalJob().getId());
            scheduleList = findByDateAndPersonalId(scheduleDto.getPersonalJob().getId().toString(), scheduleDto.getDate().toString());
            schedule.setPersonalJob(personalJob);
        }
        if (scheduleDto.getService() != null) {
            com.medCenter.medCenter.model.entity.Service service = serviceService.findById(scheduleDto.getService().getId());
            scheduleList = findByDateAndServiceId(service.getId(), Date.valueOf(scheduleDto.getDate()));
            schedule.setService(service);
        }
        if (!scheduleList.isEmpty()) {
            throw new ScheduleExistException();
        }
        scheduleRepository.save(schedule);
    }

    @Transactional
    @Override
    public void setScheduleWithFixedTimeForPeriod(String date, ScheduleDto scheduleDto) {
        LocalDate minDate = scheduleDto.getDate(); //get min date from schedule
        LocalDate maxDate = Date.valueOf(date).toLocalDate();

        while (minDate.isBefore(maxDate) || minDate.equals(maxDate)) {
            try {
                setScheduleForDay(scheduleDto); //set schedule for each day from period before selected date
            } catch (ScheduleExistException e) {
                e.getLocalizedMessage();
            }
            minDate = minDate.plusDays(Days.ONE_DAY.getDaysQuantity());
            scheduleDto.setDate(minDate);
        }
    }

    //------------------------------------------------------------------------find
    @Override
    public List<ScheduleDto> findByDateAndPersonalId(String personalJobId, String date) {
        List<Schedule> scheduleList = scheduleRepository.findByDateAndPersonalId(Integer.valueOf(personalJobId), Date.valueOf(date));
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleDto scheduleDto = scheduleForPersonalToDto(schedule);
            scheduleDtoList.add(scheduleDto);
        }
        return scheduleDtoList;
    }

    @Override
    public List<ScheduleDto> findByPeriodAndPersonalId(String date1, String date2, String personalJobId) {
        List<Schedule> scheduleList = scheduleRepository.findByDatePeriodAndPersonalId(Date.valueOf(date1), Date.valueOf(date2), Integer.valueOf(personalJobId));
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleDto scheduleDto = scheduleForPersonalToDto(schedule);
            scheduleDtoList.add(scheduleDto);
        }
        return scheduleDtoList;
    }

    @Override
    public List<ScheduleDto> findByDatePeriodAndPersonalIdNoDayOff(Date minDate, Date maxDate, Integer personalJobId) {
        List<Schedule> scheduleList = scheduleRepository.findByDatePeriodAndPersonalIdNoDayOff(minDate, maxDate, personalJobId);
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleDto scheduleDto = scheduleForPersonalToDto(schedule);
            scheduleDtoList.add(scheduleDto);
        }
        return scheduleDtoList;
    }

    @Override
    public List<ScheduleDto> findByDateAndServiceId(Integer serviceId, Date date) {
        List<Schedule> scheduleList = scheduleRepository.findByDateAndServiceId(serviceId, date);
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleDto scheduleDto = scheduleForPersonalToDto(schedule);
            scheduleDtoList.add(scheduleDto);
        }
        return scheduleDtoList;
    }

    @Override
    public List<ScheduleDto> findByServiceIdAndState(Integer serviceId, String state) throws ScheduleNotFoundException {
        List<Schedule> scheduleList = scheduleRepository.findByServiceIdAndState(serviceId, state);
        if (scheduleList.isEmpty()) throw new ScheduleNotFoundException();
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            ScheduleDto scheduleDto = scheduleForPersonalToDto(schedule);
            scheduleDtoList.add(scheduleDto);
        }
        return scheduleDtoList;
    }

    @Override
    public List<ScheduleDto> findByDatePeriodAndServiceId(Date minDate, Date maxDate, Integer serviceId) {
        return null;
    }

    @Override
    public List<ScheduleDto> findByDatePeriodAndServiceIdNoDayOff(Date minDate, Date maxDate, Integer serviceId) {
        return null;
    }

    @Transactional
    @Override
    public void setDayOff(String dayOff, Integer scheduleId) {
        scheduleRepository.updateDayOff(dayOff, scheduleId);
    }

    @Transactional
    @Override
    public void updateTime(Time startTime, Time endTime, Integer scheduleId) {
        scheduleRepository.updateTime(startTime, endTime, scheduleId);
    }

    @Transactional
    @Override
    public void updateStartTime(Time startTime, Integer scheduleId) {
        scheduleRepository.updateStartTime(startTime, scheduleId);
    }

    @Transactional
    @Override
    public void updateEndTime(Time endTime, Integer scheduleId) {
        scheduleRepository.updateEndTime(endTime, scheduleId);
    }

    @Transactional
    @Override
    public void updateState(String state, Integer scheduleId) {
        scheduleRepository.updateState(state, scheduleId);
    }

    @Transactional
    @Override
    public void updateStateByDate(String state, Date date) {
        scheduleRepository.updateStateByDate(state, date);
    }

    @Transactional
    @Override
    public void updateSchedule(Time startTime, Time endTime, Integer scheduleId) {
        scheduleRepository.updateSchedule(startTime, endTime, scheduleId);
    }


}
