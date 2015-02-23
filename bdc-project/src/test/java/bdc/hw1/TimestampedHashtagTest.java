package bdc.hw1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimestampedHashtagTest {

    @Test
    public void testReadWrite() throws Exception {
        TimestampedHashtag hashtag = new TimestampedHashtag(new Date(), "hashtag");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutput out = new DataOutputStream(buffer);
        hashtag.write(out);

        DataInput in = new DataInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        TimestampedHashtag clone = new TimestampedHashtag();
        clone.readFields(in);

        assertEquals(hashtag, clone);
    }

}
