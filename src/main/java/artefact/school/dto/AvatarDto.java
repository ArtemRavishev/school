package artefact.school.dto;

import artefact.school.entity.Student;

import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

public class AvatarDto {

    private Long id;
    private long fileSize;
    private String mediaType;
    private String avatarUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
