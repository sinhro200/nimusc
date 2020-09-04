package core.interfaces.linkToSongConverter;

import core.request.HttpUrlParameters;
import core.SongInfo;

import java.util.function.Consumer;

public interface LinkToSongConverter {

    void convert(
            HttpUrlParameters userParams,
            Consumer<SongInfo> onSongReady,
            Consumer<LinkConvertingException> onError
    );
}
