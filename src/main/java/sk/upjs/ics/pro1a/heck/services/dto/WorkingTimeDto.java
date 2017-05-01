package sk.upjs.ics.pro1a.heck.services.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkingTimeDto {

    @JsonProperty("interval")
    private short interval;

    @JsonProperty("workingTimes")
    private List<WorkingDayDto> workingTimes;

    public short getInterval() {
        return interval;
    }

    public void setInterval(short interval) {
        this.interval = interval;
    }

    public List<WorkingDayDto> getWorkingTimes() {
        return workingTimes;
    }

    public void setWorkingTimes(List<WorkingDayDto> workingTimes) {
        this.workingTimes = workingTimes;
    }
}
