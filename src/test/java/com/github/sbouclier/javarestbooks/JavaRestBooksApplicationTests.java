package com.github.sbouclier.javarestbooks;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaRestBooksApplicationTests {

	@Test
	public void contextLoads() {
        JavaRestBooksApplication.main(Arrays.array());
	}

}
