package hlf.streamerror.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "work")
public class Work {
  @Id
  @Column(name = "work_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "work_value")
  private Integer value;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public static Work work(Integer i) {
    var work = new Work();
    work.setValue(i);
    return work;
  }

  public String toString() {
    return new StringBuilder("{")
        .append("id:")
        .append(id)
        .append(", ")
        .append("val:")
        .append(value)
        .append("}")
        .toString();
  }
}
