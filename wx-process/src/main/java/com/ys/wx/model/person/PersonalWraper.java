package com.ys.wx.model.person;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

@XStreamAlias("arrResult")
public class PersonalWraper implements Serializable {

    @XStreamImplicit(itemFieldName = "psndocWXVO")
    private List<PersonalForImport> personals;

    @XStreamAlias("count")
    private String count;

    public List<PersonalForImport> getPersonals() {
        return personals;
    }

    public void setPersonals(List<PersonalForImport> personals) {
        this.personals = personals;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "PersonalWraper [personals=" + personals + ", count=" + count + "]";
    }
}
