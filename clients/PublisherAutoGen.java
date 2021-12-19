package clients;

import java.io.DataOutputStream;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import common.FinalData;

public class PublisherAutoGen implements Runnable {
    private DataOutputStream outputStream = null;

    public PublisherAutoGen(DataOutputStream _output) {
        this.outputStream = _output;
    }

    @Override
    public void run() {
        while (FinalData.isAliveThread) {
            try {
                Thread.sleep(100);
                if (FinalData.checkStartPub) {
                    try {
                        int randomNumber = ThreadLocalRandom.current().nextInt(2000, 5000);
                        // data random
                        String uuidRandom = UUID.randomUUID().toString();
                        outputStream.writeUTF(uuidRandom);
                        Thread.sleep(randomNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

}
