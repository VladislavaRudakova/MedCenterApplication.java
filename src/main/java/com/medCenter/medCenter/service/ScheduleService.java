package com.medCenter.medCenter.service;

import com.medCenter.medCenter.dto.ScheduleDto;
import com.medCenter.medCenter.exception.ScheduleExistException;
import com.medCenter.medCenter.exception.ScheduleNotFoundException;
import com.medCenter.medCenter.model.entity.Schedule;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface ScheduleService {

    ScheduleDto scheduleForPersonalToDto(Schedule schedule);

    List<ScheduleDto> findByUserId(Integer userId, String state) throws ScheduleNotFoundException;

    ScheduleDto findById(Integer id);

    List<ScheduleDto> findByPersonalId(String personalJobId) throws ScheduleNotFoundException;

    void setScheduleForDay(ScheduleDto scheduleDto) throws ScheduleExistException;

    List<ScheduleDto> findByDateAndPersonalId(String personalJobId, String date);

    void setScheduleWithFixedTimeForPeriod(String date, ScheduleDto scheduleDto);

    List<ScheduleDto> findByPeriodAndPersonalId(String date1, String date2, String personalJobId);

    List<ScheduleDto> findByDatePeriodAndPersonalIdNoDayOff(Date minDate, Date maxDate, Integer personalJobId);

    List<ScheduleDto> findByDateAndServiceId(Integer serviceId, Date date);

    List<ScheduleDto> findByServiceIdAndState(Integer serviceId,String state) throws ScheduleNotFoundException;

    List<ScheduleDto> findByDatePeriodAndServiceId(Date minDate, Date maxDate, Integer serviceId);

    List<ScheduleDto> findByDatePeriodAndServiceIdNoDayOff(Date minDate,Date maxDate, Integer serviceId);

    void setDayOff(String dayOff, Integer id);

    void updateTime(Time startTime, Time endTime, Integer scheduleId);

    void updateStartTime(Time startTime, Integer scheduleId);

    void updateEndTime(Time endTime, Integer scheduleId);

    void updateState(String state, Integer scheduleId);

    void updateStateByDate(String state, Date date);

    void updateSchedule(Time startTime, Time endTime, Integer scheduleId);

}
