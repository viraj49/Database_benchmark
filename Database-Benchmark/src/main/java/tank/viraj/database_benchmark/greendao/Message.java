package tank.viraj.database_benchmark.greendao;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.
/**
 * Entity mapped to table "MESSAGE".
 */
@Entity
public class Message {

    @Id(autoincrement = true)
    private Long id;
    private Integer int_field;
    private Long long_field;
    private Double double_field;
    private String string_field;

    @Generated(hash = 637306882)
    public Message() {
    }

    public Message(Long id) {
        this.id = id;
    }

    @Generated(hash = 33676989)
    public Message(Long id, Integer int_field, Long long_field, Double double_field, String string_field) {
        this.id = id;
        this.int_field = int_field;
        this.long_field = long_field;
        this.double_field = double_field;
        this.string_field = string_field;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInt_field() {
        return int_field;
    }

    public void setInt_field(Integer int_field) {
        this.int_field = int_field;
    }

    public Long getLong_field() {
        return long_field;
    }

    public void setLong_field(Long long_field) {
        this.long_field = long_field;
    }

    public Double getDouble_field() {
        return double_field;
    }

    public void setDouble_field(Double double_field) {
        this.double_field = double_field;
    }

    public String getString_field() {
        return string_field;
    }

    public void setString_field(String string_field) {
        this.string_field = string_field;
    }

}
