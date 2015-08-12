package jwt4j;

import java.util.Arrays;
import java.util.List;

public class TestUserBean
{
    private String username;

    private byte[] password;

    private List<TestUserBean> friends;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public byte[] getPassword()
    {
        return password;
    }

    public void setPassword(byte[] password)
    {
        this.password = password;
    }

    public List<TestUserBean> getFriends()
    {
        return friends;
    }

    public void setFriends(List<TestUserBean> friends)
    {
        this.friends = friends;
    }

    @Override
    public String toString()
    {
        return "TestUserBean{" +
                "username='" + username + '\'' +
                ", password=" + Arrays.toString(password) +
                ", friends=" + friends +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestUserBean that = (TestUserBean) o;

        if (friends != null ? !friends.equals(that.friends) : that.friends != null) return false;
        if (!Arrays.equals(password, that.password)) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? Arrays.hashCode(password) : 0);
        result = 31 * result + (friends != null ? friends.hashCode() : 0);
        return result;
    }
}