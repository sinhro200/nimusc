package nimusc.core.interfaces.songFinder;

import nimusc.core.request.HttpUrlParameters;
import nimusc.core.SongInfo;

import java.util.List;
import java.util.function.Consumer;

public interface SongFinder {

    void search(
            HttpUrlParameters userParams,
            Consumer<List<SongInfo>> onSongReady,
            Consumer<SongFinderException> onError
    );
}
