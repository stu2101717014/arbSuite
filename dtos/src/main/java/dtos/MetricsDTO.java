package dtos;

public class MetricsDTO {

    private Long dataReshapeTimeComplexity;

    private Long nameSimilaritiesRemapTimeComplexity;

    public Long getDataReshapeTimeComplexity() {
        return dataReshapeTimeComplexity;
    }

    public void setDataReshapeTimeComplexity(Long dataReshapeTimeComplexity) {
        this.dataReshapeTimeComplexity = dataReshapeTimeComplexity;
    }

    public Long getNameSimilaritiesRemapTimeComplexity() {
        return nameSimilaritiesRemapTimeComplexity;
    }

    public void setNameSimilaritiesRemapTimeComplexity(Long nameSimilaritiesRemapTimeComplexity) {
        this.nameSimilaritiesRemapTimeComplexity = nameSimilaritiesRemapTimeComplexity;
    }
}
