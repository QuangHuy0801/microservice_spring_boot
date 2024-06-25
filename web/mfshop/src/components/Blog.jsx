

const Blog = () => {
  // Sample data
  const blogItems = [
    {
        image: "img/blog/blog-1.jpg",
        date: "16 February 2020",
        title: "What Curling Irons Are The Best Ones"
      },
      {
        image: "img/blog/blog-2.jpg",
        date: "21 February 2020",
        title: "Eternity Bands Do Last Forever"
      },
      {
        image: "img/blog/blog-3.jpg",
        date: "28 February 2020",
        title: "The Health Benefits Of Sunglasses"
      },
      {
        image: "img/blog/blog-4.jpg",
        date: "16 February 2020",
        title: "Aiming For Higher The Mastopexy"
      },
      {
        image: "img/blog/blog-5.jpg",
        date: "21 February 2020",
        title: "Wedding Rings A Gift For A Lifetime"
      },
      {
        image: "img/blog/blog-6.jpg",
        date: "28 February 2020",
        title: "The Different Methods Of Hair Removal"
      },
      {
        image: "img/blog/blog-7.jpg",
        date: "16 February 2020",
        title: "Hoop Earrings A Style From History"
      },
      {
        image: "img/blog/blog-8.jpg",
        date: "21 February 2020",
        title: "Lasik Eye Surgery Are You Ready"
      },
      {
        image: "img/blog/blog-9.jpg",
        date: "28 February 2020",
        title: "Lasik Eye Surgery Are You Ready"
      },
  ];

  return (
    <>
      {/* Breadcrumb Section Begin */}
      <section className="breadcrumb-blog set-bg"  style={{ backgroundImage: `url(img/breadcrumb-bg.jpg)` }} >
        <div className="container">
          <div className="row">
            <div className="col-lg-12">
              <h2>Our Blog</h2>
            </div>
          </div>
        </div>
      </section>
      {/* Breadcrumb Section End */}

      {/* Blog Section Begin */}
      <section className="blog spad">
        <div className="container">
          <div className="row">
            {/* Mapping through blog items */}
            {blogItems.map((blogItem, index) => (
              <div className="col-lg-4 col-md-6 col-sm-6" key={index}>
                <div className="blog__item">
                <div className="blog__item__pic set-bg" style={{ backgroundImage: `url(${blogItem.image})` }}></div>
                  <div className="blog__item__text">
                    <span><img src="img/icon/calendar.png" alt="" /> {blogItem.date}</span>
                    <h5>{blogItem.title}</h5>
                    <a href="#">Read More</a>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
      {/* Blog Section End */}
    </>
  );
};

export default Blog;
