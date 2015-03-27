package sns.meme.ual.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("UalMember")
public class UalMember extends ParseObject {
	private String nickName;
	private String phoneNumber;
	private String memberId;
	private String profilePath;
    private int naeGong;

    public UalMember() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public int getNaeGong() {
        return naeGong;
    }

    public void setNaeGong(int naeGong) {
        this.naeGong = naeGong;
    }
}
