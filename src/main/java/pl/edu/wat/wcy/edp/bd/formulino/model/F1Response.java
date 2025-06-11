package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
class F1Response {
    @JsonProperty("MRData")
    private MRData mrData;

    public MRData getMrData() { return mrData; }
    public void setMrData(MRData mrData) { this.mrData = mrData; }
}
