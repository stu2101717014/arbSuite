package com.example.ui.services;

import com.example.ui.entities.*;
import com.example.ui.services.helpers.LocalDateAdapter;
import com.example.ui.services.interfaces.DataRequestService;
import com.google.gson.*;
import com.helger.commons.collection.impl.CommonsTreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UIService {

    private DataRequestService dataRequestService;

    @Autowired
    public UIService(DataRequestService dataRequestService) {
        this.dataRequestService = dataRequestService;
    }

    public List<FourPlatformsEventWrapper> getData(){
        try {
            RequestDataResult requestDataResult = new RequestDataResult();
            requestDataResult = dataRequestService.requestData(requestDataResult);
            List<FourPlatformsEventWrapper> eventWrapperTreeSet = reshapeTableTennisEventsData(requestDataResult);
            return eventWrapperTreeSet;
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    public List<FourPlatformsEventWrapper> reshapeTableTennisEventsData(RequestDataResult requestDataResult){
        TreeSet<FourPlatformsEventWrapper> eventWrapperTreeSet = new TreeSet<>();

        try {

            if (requestDataResult != null && requestDataResult.getBwinResult() != null &&requestDataResult.getBwinResult().getTableTennisEventEntitySet() != null){
                for (TableTennisEventEntity ttee : requestDataResult.getBwinResult().getTableTennisEventEntitySet()){
                    FourPlatformsEventWrapper current = new FourPlatformsEventWrapper();
                    current.setEventDate(ttee.getEventDate());
                    current.setFirstPlayer(ttee.getFirstPlayerName());
                    current.setSecondPlayer(ttee.getSecondPlayerName());
                    current.setBwinEvent(this.TableTennisEventToString(ttee));

                    eventWrapperTreeSet.add(current);
                }
            }

            if (requestDataResult != null && requestDataResult.getBets22Result() != null && requestDataResult.getBets22Result().getTableTennisEventEntitySet() != null){
                for(TableTennisEventEntity ttee : requestDataResult.getBets22Result().getTableTennisEventEntitySet()){
                    FourPlatformsEventWrapper current = new FourPlatformsEventWrapper();
                    current.setEventDate(ttee.getEventDate());
                    current.setFirstPlayer(ttee.getFirstPlayerName());
                    current.setSecondPlayer(ttee.getSecondPlayerName());

                    Optional<FourPlatformsEventWrapper> first = eventWrapperTreeSet.stream().filter(wr -> wr.equals(current)).findFirst();
                    if (first.isPresent()){
                        FourPlatformsEventWrapper fourPlatformsEventWrapper = first.get();
                        eventWrapperTreeSet.remove(fourPlatformsEventWrapper);
                        fourPlatformsEventWrapper.setBets22Event(this.TableTennisEventToString(ttee));
                        eventWrapperTreeSet.add(fourPlatformsEventWrapper);
                    }else {
                        current.setBets22Event(this.TableTennisEventToString(ttee));
                        eventWrapperTreeSet.add(current);
                    }
                }
            }

            if (requestDataResult != null && requestDataResult.getBetwinnerResult() != null && requestDataResult.getBetwinnerResult().getTableTennisEventEntitySet() != null){
                for(TableTennisEventEntity ttee : requestDataResult.getBetwinnerResult().getTableTennisEventEntitySet()){
                    FourPlatformsEventWrapper current = new FourPlatformsEventWrapper();
                    current.setEventDate(ttee.getEventDate());
                    current.setFirstPlayer(ttee.getFirstPlayerName());
                    current.setSecondPlayer(ttee.getSecondPlayerName());

                    Optional<FourPlatformsEventWrapper> first = eventWrapperTreeSet.stream().filter(wr -> wr.equals(current)).findFirst();
                    if (first.isPresent()){
                        FourPlatformsEventWrapper fourPlatformsEventWrapper = first.get();
                        eventWrapperTreeSet.remove(fourPlatformsEventWrapper);
                        fourPlatformsEventWrapper.setBetWinnerEvent(this.TableTennisEventToString(ttee));
                        eventWrapperTreeSet.add(fourPlatformsEventWrapper);
                    }else {
                        current.setBetWinnerEvent(this.TableTennisEventToString(ttee));
                        eventWrapperTreeSet.add(current);
                    }
                }
            }


        }catch (Exception e){

        }

        List<FourPlatformsEventWrapper> collect = eventWrapperTreeSet.stream()
                .sorted(Comparator.comparing(FourPlatformsEventWrapper::getSecondPlayer))
                .sorted(Comparator.comparing(FourPlatformsEventWrapper::getFirstPlayer))
                .sorted(Comparator.comparing(FourPlatformsEventWrapper::getEventDate))
                .collect(Collectors.toList());



        return collect;
    }

    private String TableTennisEventToString(TableTennisEventEntity ttee){
        return ttee.getFirstPlayerName() + " - " + ttee.getSecondPlayerName() + "\n\r" + ttee.getFirstPlayerWinningOdd() + " - " + ttee.getSecondPlayerWinningOdd();
    }
}
