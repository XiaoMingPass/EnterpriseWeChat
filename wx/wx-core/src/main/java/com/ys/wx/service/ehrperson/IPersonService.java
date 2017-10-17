package com.ys.wx.service.ehrperson;

import com.mhome.tools.vo.ReturnVo;
import com.ys.wx.model.person.PersonalForImport;

import java.util.List;

public interface IPersonService {

	@Deprecated
    ReturnVo<List<PersonalForImport>> getEHRPersonalData() throws Exception;
	@Deprecated
    ReturnVo<List<PersonalForImport>> getEHRPersonalData(int page) throws Exception;
	@Deprecated
    ReturnVo<List<PersonalForImport>> getEHRPersonalDataByTime(String time) throws Exception;
}
