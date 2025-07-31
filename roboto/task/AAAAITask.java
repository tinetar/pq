package dre.elfocrash.roboto.task;

import java.util.List;
import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.FFFFakePlayerManager;

public class AAAAITask implements Runnable
{   
    private int _from;
    private int _to;

    public AAAAITask(int from, int to) {
        _from = from;
        _to = to;
    }

    @Override
    public void run()
    {
        adjustPotentialIndexOutOfBounds();

        try {
            List<FFFFakePlayer> ffffakePlayers = FFFFakePlayerManager.INSTANCE
                .getFFFFakePlayers()
                .subList(_from, _to);

            ffffakePlayers
                .stream()
                .filter(bot -> !bot.getFFFFakeAi().isBusyThinking())
                .forEach(bot -> bot.getFFFFakeAi().thinkAndAct());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void adjustPotentialIndexOutOfBounds() {
        int size = FFFFakePlayerManager.INSTANCE.getFFFFakePlayersCount();

        if (_from > size) {
            _from = size;
        }
        if (_to > size) {
            _to = size;
        }
        if (_from > _to) {
            _from = _to;
        }
    }
}
