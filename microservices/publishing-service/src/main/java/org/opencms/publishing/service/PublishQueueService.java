package org.opencms.publishing.service;

import org.opencms.publishing.entity.PublishJobEntity;
import org.opencms.publishing.repository.PublishJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PublishQueueService {
    
    @Autowired
    private PublishJobRepository publishJobRepository;
    
    @Transactional
    public void enqueueJob(PublishJobEntity job) {
        job.setEnqueueTime(System.currentTimeMillis());
        publishJobRepository.save(job);
    }
    
    public List<PublishJobEntity> getAllQueuedJobs() {
        return publishJobRepository.findAll();
    }
    
    public Optional<PublishJobEntity> getJob(String historyId) {
        return publishJobRepository.findById(historyId);
    }
    
    @Transactional
    public void removeJob(String historyId) {
        publishJobRepository.deleteById(historyId);
    }
    
    @Transactional
    public void updateJob(PublishJobEntity job) {
        publishJobRepository.save(job);
    }
}
