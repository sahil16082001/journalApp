package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveNewEntry(JournalEntry journalEntry, String userName){
        try {
            User user=userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved=journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);

        } catch (Exception e){
            log.error("Exception",e);
            throw new RuntimeException("An error occured while saving the entry.",e);
        }

    }

    public void  saveEntry(JournalEntry journalEntry){
        try {
            journalEntry.setDate(LocalDateTime.now());
           journalEntryRepository.save(journalEntry);

        } catch (Exception e){
            log.error("Exception",e);
        }
    }

    public List<JournalEntry> getAll(){
        try {
            List<JournalEntry> res = journalEntryRepository.findAll();
            return res;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Optional<JournalEntry> findJournalEntryById(ObjectId id){
        Optional<JournalEntry> myEntry=journalEntryRepository.findById(id);
        return myEntry;
    }

    @Transactional
    public boolean deleteById(ObjectId id,String userName){
        boolean removed=false;
        try {
            User user=userService.findByUserName(userName);
            removed=user.getJournalEntries().removeIf(x-> x.getId().equals(id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e){
            throw new RuntimeException("An error occured while deleting an entry.",e);
        }

        return removed;
    }



}
