package core.interfaces.linkToSongConverter;

import core.InputParams;
import core.SongInfo;

import java.util.function.Consumer;

public interface LinkToSongConverter {

    void convert(
            InputParams inputParams,
            Consumer<SongInfo> onSongReady,
            Consumer<LinkConvertingException> onError
    );
}
