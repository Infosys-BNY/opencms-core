package org.opencms.publishing.service;

import org.opencms.publishing.entity.PublishJobEntity;
import org.opencms.publishing.repository.PublishJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PublishHistoryService {
    
    @Autowired
    private PublishJobRepository publishJobRepository;
    
    @Cacheable(value = "publishHistory", key = "#historyId")
    public Optional<PublishJobEntity> getPublishJob(String historyId) {
        return publishJobRepository.findById(historyId);
    }
    
    @CacheEvict(value = "publishHistory", allEntries = true)
    @Transactional
    public void addToHistory(PublishJobEntity publishJob) {
        publishJobRepository.save(publishJob);
    }
    
    @CacheEvict(value = "publishHistory", key = "#historyId")
    @Transactional
    public void removeFromHistory(String historyId) {
        publishJobRepository.deleteById(historyId);
    }
    
    public List<PublishJobEntity> getAllHistory() {
        return publishJobRepository.findAllFinishedJobsOrderedByEnqueueTime();
    }
}
