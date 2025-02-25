package com.koreaIT.BAM;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.BAM.dto.Article;

public class Main {
	
	private static List<Article> articles;
	
	static {
		articles = new ArrayList<>();
	}
	
	public static void main(String[] args) {
		
		System.out.println("== 프로그램 시작 ==");
		
		Scanner sc = new Scanner(System.in);
		
		int lastArticleId = 0;
		
		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine();
			
			if (cmd.equals("exit")) {
				break;
			}
			
			if (cmd.equals("article write")) {
				System.out.println("== 게시물 작성 ==");
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				lastArticleId++;
				
				Article article = new Article(lastArticleId, title, body);
				
				articles.add(article);
				
				System.out.printf("%d번 게시물이 생성되었습니다\n", lastArticleId);
				
			} else if (cmd.equals("article list")) {
				if (articles.size() == 0) {
					System.out.println("게시물이 존재하지 않습니다");
					continue;
				}
				
				System.out.println("== 게시물 목록 ==");
				System.out.println("번호	|	제목");
				for (int i = articles.size() - 1; i >= 0; i--) {
					Article article = articles.get(i);
					System.out.printf("%d	|	%s\n", article.getId(), article.getTitle());
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
}