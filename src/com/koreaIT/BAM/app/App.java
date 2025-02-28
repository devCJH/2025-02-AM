package com.koreaIT.BAM.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.BAM.dto.Article;
import com.koreaIT.BAM.dto.Member;
import com.koreaIT.BAM.session.Session;
import com.koreaIT.BAM.util.Util;

public class App {
	private List<Article> articles;
	private List<Member> members;
	private int lastArticleId;
	private int lastMemberId;
	private Member loginedMember;

	public App() {
		this.articles = new ArrayList<>();
		this.members = new ArrayList<>();
		this.lastArticleId = 0;
		this.lastMemberId = 0;
		this.loginedMember = Session.loginedMember;
	}
	
	public void run() {
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
				if (loginedMember == null) {
					System.out.println("로그인 후 이용해주세요");
					continue;
				}

				System.out.println("== 게시물 작성 ==");
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				lastArticleId++;

				Article article = new Article(lastArticleId, Util.getDateStr(), loginedMember.getId(), title, body);

				articles.add(article);

				System.out.printf("%d번 게시물이 생성되었습니다\n", lastArticleId);

			} else if (cmd.startsWith("article list")) {
				if (articles.size() == 0) {
					System.out.println("게시물이 존재하지 않습니다");
					continue;
				}

				String searchKeyword = cmd.substring("article list".length()).trim();
				
				List<Article> printArticles = articles;
				
				if (searchKeyword.length() > 0) {

					System.out.println("검색어 : " + searchKeyword);
					
					printArticles = new ArrayList<>();
					
					for (Article article : articles) {
						if (article.getTitle().contains(searchKeyword)) {
							printArticles.add(article);
						}
					}
					
					if (printArticles.size() == 0) {
						System.out.println("검색 결과가 없습니다");
						continue;
					}
				}
				
				System.out.println("== 게시물 목록 ==");
				System.out.println("번호	|	제목	|	작성자	|	작성일");
				for (int i = printArticles.size() - 1; i >= 0; i--) {
					Article article = printArticles.get(i);
					
					String writerName = getWriterNameById(article.getMemberId());
					
					System.out.printf("%d	|	%s	|	%s	|%s\n", article.getId(), article.getTitle(), writerName, article.getRegDate());
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

				String writerName = getWriterNameById(foundArticle.getMemberId());

				System.out.println("== 게시물 상세보기 ==");
				System.out.printf("번호 : %d\n", foundArticle.getId());
				System.out.printf("작성일 : %s\n", foundArticle.getRegDate());
				System.out.printf("작성자 : %s\n", writerName);
				System.out.printf("제목 : %s\n", foundArticle.getTitle());
				System.out.printf("내용 : %s\n", foundArticle.getBody());

			} else if (cmd.startsWith("article modify ")) {
				if (loginedMember == null) {
					System.out.println("로그인 후 이용해주세요");
					continue;
				}
				
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

				if (foundArticle.getMemberId() != loginedMember.getId()) {
					System.out.println("해당 게시물에 대한 권한이 없습니다");
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
				if (loginedMember == null) {
					System.out.println("로그인 후 이용해주세요");
					continue;
				}
				
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
				
				if (foundArticle.getMemberId() != loginedMember.getId()) {
					System.out.println("해당 게시물에 대한 권한이 없습니다");
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
	
	private String getWriterNameById(int memberId) {
		for (Member member : this.members) {
			if (memberId == member.getId()) {
				return member.getLoginId();
			}
		}
		return null;
	}

	private int getCmdNum(String cmd) {
		try {
			String[] cmdBits = cmd.split(" ");
			int id = Integer.parseInt(cmdBits[2]);
			return id;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private Article getArticleById(int id) {
		for (Article article : this.articles) {
			if (article.getId() == id) {
				return article;
			}
		}
		return null;
	}

	private void makeTestData() {
		System.out.println("테스트용 게시물 데이터가 5개 생성되었습니다");
		System.out.println("테스트용 회원 데이터가 5개 생성되었습니다");

		for (int i = 1; i <= 5; i++) {
			articles.add(new Article(++this.lastArticleId, Util.getDateStr(), i, "테스트" + i, "테스트" + i));
			members.add(new Member(++this.lastMemberId, Util.getDateStr(), "test" + i, "test" + i, "유저" + i));
		}
	}
}
