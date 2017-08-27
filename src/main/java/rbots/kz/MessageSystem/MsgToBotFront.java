package rbots.kz.MessageSystem;


import rbots.kz.BotService.BotService;

abstract class MsgToBotFront extends Msg {
    MsgToBotFront(Address from, Address to) { super(from, to); }

    void exec(Abonent abonent) {
        if(abonent instanceof BotService){
            exec((BotService) abonent);
        }
    }

    abstract void exec(BotService botService);


}
