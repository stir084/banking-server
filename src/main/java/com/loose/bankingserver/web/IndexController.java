package com.loose.bankingserver.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@Slf4j
public class IndexController {

  /*  @GetMapping("/user/{id}")
    public ResponseEntity<MyDto> findByid(@PathVariable Long id) {
        User user = service.getUser();
        MyDto dto = new MyDto();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        dto.setStatus(StatusEnum.OK);
        dto.setData(user);
        dto.setMessage("메세지메세지!");

        return new ResponseEntity<>(dto, header, HttpStatus.OK);
    }*/

    @GetMapping("/")
    public ResponseEntity getAllUsers() {
        return new ResponseEntity(dto, header, HttpStatus.OK);
    }



}
