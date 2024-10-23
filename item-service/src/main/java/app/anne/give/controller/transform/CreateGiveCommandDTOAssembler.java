package app.anne.give.controller.transform;

import app.anne.give.controller.dto.GiveResponse;
import app.anne.give.infrastructure.GiveRecord;

public class CreateGiveCommandDTOAssembler {
    public static GiveResponse toDTOFromCommand(GiveRecord giveRecord) {
        return new GiveResponse(giveRecord);
    }
}
