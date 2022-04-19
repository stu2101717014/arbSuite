package dtos;

public class NamesSimilaritiesDTO {

    private Long id;

    private String universalPlayerName;

    private String platformName;

    private String platformSpecificPlayerName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniversalPlayerName() {
        return universalPlayerName;
    }

    public void setUniversalPlayerName(String universalPlayerName) {
        this.universalPlayerName = universalPlayerName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformSpecificPlayerName() {
        return platformSpecificPlayerName;
    }

    public void setPlatformSpecificPlayerName(String platformSpecificPlayerName) {
        this.platformSpecificPlayerName = platformSpecificPlayerName;
    }
}
