package bdc.hw1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.hadoop.io.Writable;

public class TimestampedHashtag implements Writable {
    private long timestamp;
    private String hashtag;

    public TimestampedHashtag() {
    }

    public TimestampedHashtag(Date timestamp, String hashtag) {
        this.timestamp = timestamp.getTime();
        this.hashtag = hashtag;
    }

    public Date getTimestamp() {
        return new Date(timestamp);
    }

    public String getHashtag() {
        return hashtag;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(timestamp);
        byte[] bytes = hashtag.getBytes("UTF8");
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        timestamp = in.readLong();
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readFully(bytes);
        hashtag = new String(bytes, Charset.forName("UTF8"));
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = ((hashtag == null) ? 0 : hashtag.hashCode());
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TimestampedHashtag)) {
            return false;
        }
        TimestampedHashtag other = (TimestampedHashtag) obj;
        if (hashtag == null) {
            if (other.hashtag != null) {
                return false;
            }
        } else if (!hashtag.equals(other.hashtag)) {
            return false;
        }
        if (timestamp != other.timestamp) {
            return false;
        }
        return true;
    }
    
    
}