package com.koreaIT.BAM.session;

import com.koreaIT.BAM.dto.Member;

public class Session {
	public static Member loginedMember;
	
	static {
		loginedMember = null;
	}
}
