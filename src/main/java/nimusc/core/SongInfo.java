package nimusc.core;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SongInfo {
    private String link;
    private String name;
    private String artist;
    private int duration;
}
