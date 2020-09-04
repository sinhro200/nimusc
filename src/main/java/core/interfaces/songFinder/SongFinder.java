package core.interfaces.songFinder;

import core.request.HttpUrlParameters;
import core.SongInfo;

import java.util.List;
import java.util.function.Consumer;

public interface SongFinder {

    void search(
            HttpUrlParameters userParams,
            Consumer<List<SongInfo>> onSongReady,
            Consumer<SongFinderException> onError
    );
}
