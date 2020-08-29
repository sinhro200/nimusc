package core.interfaces.songFinder;

import core.InputParams;
import core.SongInfo;

import java.util.List;
import java.util.function.Consumer;

public interface SongFinder {

    void search(
            InputParams params,
            Consumer<List<SongInfo>> onSongReady,
            Consumer<SongFinderException> onError
    );
}
