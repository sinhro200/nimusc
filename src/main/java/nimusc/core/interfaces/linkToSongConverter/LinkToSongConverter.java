package nimusc.core.interfaces.linkToSongConverter;

import nimusc.core.request.HttpUrlParameters;
import nimusc.core.SongInfo;

import java.util.function.Consumer;

public interface LinkToSongConverter {

    void convert(
            HttpUrlParameters userParams,
            Consumer<SongInfo> onSongReady,
            Consumer<LinkConvertingException> onError
    );
}
