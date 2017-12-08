select cat1name,cat2name,count(b.asin) from book b,category c where b.categoryid=c.id group by cat1name,cat2name;

select title,star from book where commentNum > 20 order by star desc limit 5;
