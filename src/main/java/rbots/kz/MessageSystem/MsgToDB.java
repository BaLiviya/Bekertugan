package rbots.kz.MessageSystem;


import rbots.kz.DBService.DBService;

abstract class MsgToDB extends Msg {

    MsgToDB(Address from, Address to) {
        super(from, to);
    }

    void exec(Abonent abonent) {
        if(abonent instanceof DBService){
            exec((DBService) abonent);
        }
    }

    abstract void exec(DBService dbService);
}