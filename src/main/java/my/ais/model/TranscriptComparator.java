package my.ais.model;

import java.io.Serializable;
import java.util.Comparator;

import my.ais.domain.Transcript;

public class TranscriptComparator implements Comparator<Transcript>, Serializable {

	private static final long serialVersionUID = 1L;

	public int compare(Transcript o1, Transcript o2) {
		return o1.getTranscriptId().getSem().compareTo(o2.getTranscriptId().getSem().toString());
	}
	
	public int compareGroup(Transcript o1, Transcript o2) {
        if(o1.getTranscriptId().getSem().equals(o2.getTranscriptId().getSem()))
            return 0;
        else
            return 1;
    }

}
