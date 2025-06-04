

flatpickr("#date", {});

    const selectService = document.getElementById("service")
    const selectDoctor = document.getElementById("doctorSpec")


    selectService.addEventListener("change", function(){
    const value = selectService.value;

    if (value==="doctor appointment"){
    selectDoctor.style.display='flex';
    } else {
    selectDoctor.style.display='none';
}

})