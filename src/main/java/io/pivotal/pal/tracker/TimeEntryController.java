package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    private  final CounterService counterService;

    private  final GaugeService gaugeService;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,CounterService counterService,GaugeService gaugeService){
        this.timeEntryRepository = timeEntryRepository;
        this.counterService = counterService;
        this.gaugeService = gaugeService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntry){
        counterService.increment("timeentries.service.create.invoked");
        gaugeService.submit("timeentries.storage.size",timeEntryRepository.list().size());
        return new ResponseEntity<>(timeEntryRepository.create(timeEntry), HttpStatus.CREATED);

    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id){
        TimeEntry retrievedTimeEntry = timeEntryRepository.find(id);
        if (retrievedTimeEntry != null) {
            counterService.increment("timeentries.service.read.invoked");
            return new ResponseEntity<>(retrievedTimeEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list(){
        counterService.increment("timeentries.service.list.invoked");
        return new ResponseEntity<>(timeEntryRepository.list(), HttpStatus.OK);

    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TimeEntry timeEntry){

        TimeEntry timeEntryUpdated = timeEntryRepository.update(id, timeEntry);
        if (timeEntryUpdated != null) {
            counterService.increment("timeentries.service.update.invoked");
            return new ResponseEntity<>(timeEntryUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id){
        timeEntryRepository.delete(id);
        counterService.increment("timeentries.service.delete.invoked");
        gaugeService.submit("timeentries.storage.size",timeEntryRepository.list().size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
