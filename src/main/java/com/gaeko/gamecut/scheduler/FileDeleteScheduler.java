package com.gaeko.gamecut.scheduler;

import com.gaeko.gamecut.repository.FileRepository;
import com.gaeko.gamecut.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileDeleteScheduler {
    private final FileUploadService fileUploadService;
    private final FileRepository fileRepository;

    //월요일 새벽 4시 30분마다 실행
    @Scheduled(cron = "0 30 4 ? * MON")
    //@Scheduled(cron = "*/10 * * * * *")

    public void fileDelete() {
        fileUploadService.NotUsedFileDelete();
        fileUploadService.NotUsedDbDelete();
    }
}
