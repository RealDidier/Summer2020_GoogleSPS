// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map; 
import java.lang.*; 
import java.io.*; 

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

        //NoAttendees
        if( request.getAttendees().size() == 0)
        {
            return Arrays.asList(TimeRange.WHOLE_DAY);
        }
        //Duration longer than a day
        else if( request.getDuration() >= TimeRange.WHOLE_DAY.duration() + 1)
        { 
            return Arrays.asList(); 
        }

        ArrayList<TimeRange> SortedBusyTime = new ArrayList<TimeRange>(); 
        Collection<TimeRange> BusySet = new HashSet<>(); 
        ArrayList<TimeRange> TimeSlotsForMeeting = new ArrayList<TimeRange>(); 

        for(Event temp: events)
        {
            for( String attendee: temp.getAttendees())
            {
                if( request.getAttendees().contains(attendee))
                {
                    BusySet.add(temp.getWhen()); 
                     
                }
            }
        }

        for( TimeRange e: BusySet)
        {
            SortedBusyTime.add(e); 
        }
      
        Collections.sort(SortedBusyTime, TimeRange.ORDER_BY_END);

        //Finding posisible time for the meeting 

        //Ignore people not attending or when there is not conflicts 
        if( SortedBusyTime.size() ==0 && request.getAttendees().size() != 0)
        {
            return Arrays.asList(TimeRange.WHOLE_DAY); 
        }

        if( SortedBusyTime.get(0).start() != 0)
        {
            if( (SortedBusyTime.get(0).start() - TimeRange.START_OF_DAY ) >= request.getDuration())
            {
                TimeSlotsForMeeting.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, SortedBusyTime.get(0).start(), false)); 
            }

        }
        if(SortedBusyTime.get(SortedBusyTime.size()-1).end() < 1400)
        {
            if( ( TimeRange.END_OF_DAY - SortedBusyTime.get(SortedBusyTime.size()-1).end() )  >= request.getDuration() )
            {
                TimeSlotsForMeeting.add(TimeRange.fromStartEnd(SortedBusyTime.get(SortedBusyTime.size()-1).end(), TimeRange.END_OF_DAY, true));                
            }
            
        }

        int k = 0; 
        for(int i =1; i<SortedBusyTime.size(); i++)
        {
            if(SortedBusyTime.get(i).start() > SortedBusyTime.get(k).end())
            {

                if( (SortedBusyTime.get(i).start() - SortedBusyTime.get(k).end()) >= request.getDuration()  ) 
                {
                    TimeSlotsForMeeting.add(TimeRange.fromStartEnd(SortedBusyTime.get(k).end(), SortedBusyTime.get(i).start(), false )); 
                    k = i;
                }
 
            }
            else if( SortedBusyTime.get(i).start() < SortedBusyTime.get(k).end() ) //Nested events 
            {
                if( SortedBusyTime.get(k).start() > SortedBusyTime.get(i).start()  )
                {
                    if( i-1 == 0)
                    {
                        if( (SortedBusyTime.get(i).start() - TimeRange.START_OF_DAY ) >= request.getDuration())
                        {
                            TimeSlotsForMeeting.set(0, TimeRange.fromStartEnd(TimeRange.START_OF_DAY , SortedBusyTime.get(i).start(), false) ); 
                            k = i; 
                        }

                    }
  
                }
            }


        }
        Collections.sort(TimeSlotsForMeeting, TimeRange.ORDER_BY_END);
        return TimeSlotsForMeeting; 

    
  }
}
