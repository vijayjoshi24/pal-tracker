package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository){
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntry){

        return new ResponseEntity<>(timeEntryRepository.create(timeEntry), HttpStatus.CREATED);

    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id){
        TimeEntry retrievedTimeEntry = timeEntryRepository.find(id);
        if (retrievedTimeEntry != null) {
            return new ResponseEntity<>(retrievedTimeEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list(){

        return new ResponseEntity<>(timeEntryRepository.list(), HttpStatus.OK);

    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TimeEntry timeEntry){

        TimeEntry timeEntryUpdated = timeEntryRepository.update(id, timeEntry);
        if (timeEntryUpdated != null) {
            return new ResponseEntity<>(timeEntryUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id){
        timeEntryRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
