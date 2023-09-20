package artefact.school.controller;


import artefact.school.dto.AvatarDto;
import artefact.school.service.AvatarService;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AvatarController.BASE_PATH)
public class AvatarController {

    public static final String BASE_PATH = "avatars";
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping("/{id}/from-db")
    public ResponseEntity<byte[]> getFromDb(@PathVariable long id) {
        return build(avatarService.getFromDb(id));
    }


    @GetMapping("/{id}/from-fs")
    public ResponseEntity<byte[]> getFromFs(@PathVariable long id) {
        return build(avatarService.getFromFs(id));
    }

    @GetMapping
    public List<AvatarDto> getPage(@RequestParam(value = "page ",required = false,defaultValue = "0") int page,
                                   @RequestParam(value = "size ",required = false,defaultValue = "10")int size) {
        return avatarService.getPage(Math.abs(page),Math.abs(size));
    }

    private ResponseEntity<byte[]>build(Pair<byte[], String> pair){
        byte[] data = pair.getFirst();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .contentLength(data.length)
                .body(data);
    }

}
