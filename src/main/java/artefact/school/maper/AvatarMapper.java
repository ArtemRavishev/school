package artefact.school.maper;


import artefact.school.controller.AvatarController;
import artefact.school.dto.AvatarDto;
import artefact.school.entity.Avatar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AvatarMapper {

    private final int port;

    public AvatarMapper(@Value("${server.port}") int port) {
        this.port = port;
    }

    public AvatarDto toDto(Avatar avatar){
        AvatarDto avatarDto = new AvatarDto();
        avatarDto.setId(avatar.getId());
        avatarDto.setFileSize(avatar.getFileSize());
        avatarDto.setMediaType(avatar.getMediaType());
        avatarDto.setAvatarUrl(  UriComponentsBuilder.newInstance().host("localhost")
                .scheme("http")
                .host("localhost")
                .port(port)
                .pathSegment(AvatarController.BASE_PATH,avatar.getId().toString(),"from-db")
                .toUriString());
        return avatarDto;


    }
}
