package com.koreaIT.BAM;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.BAM.dto.Article;
import com.koreaIT.BAM.dto.Member;
import com.koreaIT.BAM.util.Util;

public class Main {
	
	private static List<Article> articles;
	private static List<Member> members;
	private static int lastArticleId;
	private static int lastMemberId;
	private static Member loginedMember;
	
	static {
		articles = new ArrayList<>();
		members = new ArrayList<>();
		lastArticleId = 0;
		lastMemberId = 0;
		loginedMember = null;
	}
	
	public static void main(String[] args) {
		
		System.out.println("== 프로그램 시작 ==");
		
		makeTestData();
		
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine().trim();
			
			if (cmd.equals("exit")) {
				break;
			}
			
			if (cmd.length() == 0) {
				System.out.println("명령어를 입력해주세요");
				continue;
			}
			
			if (cmd.equals("member join")) {
				if (loginedMember != null) {
					System.out.println("로그아웃 후 이용해주세요");
					continue;
				}
				
				System.out.println("== 회원 가입 ==");
				
				String loginId = null;
				String loginPw = null;
				String name = null;
				
				while (true) {
					System.out.printf("로그인 아이디 : ");
					loginId = sc.nextLine().trim();
					
					if (loginId.length() == 0) {
						System.out.println("아이디는 필수 입력 정보입니다");
						continue;
					}
					
					boolean isLoginIdDup = false;
					
					for (Member member : members) {
						if (loginId.equals(member.getLoginId())) {
							isLoginIdDup = true;
							break;
						}
					}
					
					if (isLoginIdDup) {
						System.out.printf("[ %s ]은(는) 이미 사용중인 아이디입니다\n", loginId);
						continue;
					}
					
					System.out.printf("[ %s ]은(는) 사용가능한 아이디입니다\n", loginId);
					break;
				}
				
				while (true) {
					System.out.printf("로그인 비밀번호 : ");
					loginPw = sc.nextLine().trim();
					
					if (loginPw.length() == 0) {
						System.out.println("비밀번호는 필수 입력 정보입니다");
						continue;
					}
					
					System.out.printf("비밀번호 확인 : ");
					String loginPwChk = sc.nextLine();
					
					if (!loginPw.equals(loginPwChk)) {
						System.out.println("비밀번호가 일치하지 않습니다");
						continue;
					}
					
					break;
				}
				
				while (true) {
					System.out.printf("이름 : ");
					name = sc.nextLine().trim();
					
					if (name.length() == 0) {
						System.out.println("이름은 필수 입력 정보입니다");
						continue;
					}
					
					break;
				}
				
				lastMemberId++;
				
				Member member = new Member(lastMemberId, Util.getDateStr(), loginId, loginPw, name);
				
				members.add(member);
				
				System.out.printf("%s님의 가입이 완료되었습니다\n", loginId);
				
			} else if (cmd.equals("member login")) {
				if (loginedMember != null) {
					System.out.println("로그아웃 후 이용해주세요");
					continue;
				}
				
				System.out.println("== 로그인 ==");
				System.out.printf("아이디 : ");
				String loginId = sc.nextLine().trim();
				System.out.printf("비밀번호 : ");
				String loginPw = sc.nextLine().trim();
				
				if (loginId.length() == 0) {
					System.out.println("아이디를 입력해주세요");
					continue;
				}
				
				if (loginPw.length() == 0) {
					System.out.println("비밀번호를 입력해주세요");
					continue;
				}
				
				Member foundMember = null;
				
				for (Member member : members) {
					if (loginId.equals(member.getLoginId())) {
						foundMember = member;
						break;
					}
				}
				
				if (foundMember == null) {
					System.out.printf("[ %s ]은(는) 존재하지 않는 아이디입니다\n", loginId);
					continue;
				}
				
				if (!foundMember.getLoginPw().equals(loginPw)) {
					System.out.println("비밀번호가 일치하지 않습니다");
					continue;
				}
				
				loginedMember = foundMember;
				System.out.printf("[ %s ]님 환영합니다~\n", foundMember.getName());
				
			} else if (cmd.equals("member logout")) {
				if (loginedMember == null) {
					System.out.println("로그인 후 이용해주세요");
					continue;
				}
				
				loginedMember = null;
				System.out.println("정상적으로 로그아웃 되었습니다");
				
			} else if (cmd.equals("article write")) {
				System.out.println("== 게시물 작성 ==");
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				lastArticleId++;
				
				Article article = new Article(lastArticleId, Util.getDateStr(), title, body);
				
				articles.add(article);
				
				System.out.printf("%d번 게시물이 생성되었습니다\n", lastArticleId);
				
			} else if (cmd.equals("article list")) {
				if (articles.size() == 0) {
					System.out.println("게시물이 존재하지 않습니다");
					continue;
				}
				
				System.out.println("== 게시물 목록 ==");
				System.out.println("번호	|	제목	|	작성일");
				for (int i = articles.size() - 1; i >= 0; i--) {
					Article article = articles.get(i);
					System.out.printf("%d	|	%s	|%s\n", article.getId(), article.getTitle(), article.getRegDate());
				}
				
			} else if (cmd.startsWith("article detail ")) {
				int id = getCmdNum(cmd);
				
				if (id == -1) {
					System.out.println("잘못된 명령어입니다");
					continue;
				}
				
				Article foundArticle = getArticleById(id);
				
				if (foundArticle == null) {
					System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
					continue;
				}
				
				System.out.println("== 게시물 상세보기 ==");
				System.out.printf("번호 : %d\n", foundArticle.getId());
				System.out.printf("작성일 : %s\n", foundArticle.getRegDate());
				System.out.printf("제목 : %s\n", foundArticle.getTitle());
				System.out.printf("내용 : %s\n", foundArticle.getBody());
				
			} else if (cmd.startsWith("article modify ")) {
				int id = getCmdNum(cmd);
				
				if (id == -1) {
					System.out.println("잘못된 명령어입니다");
					continue;
				}
				
				Article foundArticle = getArticleById(id);
				
				if (foundArticle == null) {
					System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
					continue;
				}
				
				System.out.println("== 게시물 수정 ==");
				System.out.printf("수정할 제목 : ");
				String title = sc.nextLine();
				System.out.printf("수정할 내용 : ");
				String body = sc.nextLine();

				foundArticle.setTitle(title);
				foundArticle.setBody(body);
				System.out.printf("%d번 게시물을 수정했습니다\n", id);
				
			} else if (cmd.startsWith("article delete ")) {
				int id = getCmdNum(cmd);
				
				if (id == -1) {
					System.out.println("잘못된 명령어입니다");
					continue;
				}

				Article foundArticle = getArticleById(id);
				
				if (foundArticle == null) {
					System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
					continue;
				}
				
				articles.remove(foundArticle);
				
				System.out.printf("%d번 게시물을 삭제했습니다\n", id);
				
			} else {
				System.out.println("존재하지 않는 명령어 입니다");
			}
		}
		
		sc.close();
		
		System.out.println("== 프로그램 종료 ==");
	}

	private static int getCmdNum(String cmd) {
		try {
			String[] cmdBits = cmd.split(" ");
			int id = Integer.parseInt(cmdBits[2]);
			return id;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private static Article getArticleById(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				return article;
			}
		}
		return null;
	}
	
	private static void makeTestData() {
		System.out.println("테스트용 게시물 데이터가 5개 생성되었습니다");
		
		for (int i = 1; i <= 5; i++) {
			articles.add(new Article(++lastArticleId, Util.getDateStr(), "테스트 제목" + i, "테스트 내용" + i));
		}
	}
}