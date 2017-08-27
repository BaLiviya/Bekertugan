package rbots.kz.MessageSystem;

import rbots.kz.BotSessionService.BotSessionService;

abstract class MsgToBotSS extends Msg{

    MsgToBotSS(Address from, Address to) {
        super(from, to);
    }

    void exec(Abonent abonent) {
        if(abonent instanceof BotSessionService){
            exec((BotSessionService) abonent);
        }
    }

    abstract void exec(BotSessionService botSessionService);
}