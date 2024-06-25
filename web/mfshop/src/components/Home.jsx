import { useState, useEffect } from 'react';
import { listbsProduct, listnewProduct } from '../services/ProductService';
import { addToCart } from '../services/CartService';
import { ToastContainer, toast } from 'react-toastify';


const Home = () => {
    
    const [newProducts, setNewProducts] = useState([]);
    const [bestSellerProducts, setBestSellerProducts] = useState([]);
    const [error, setError] = useState(null);
    const [addToCartError, setAddToCartError] = useState(null);
    const [filterState, setFilterState] = useState('new-arrivals');
    const [countdown, setCountdown] = useState({ days: 0, hours: 0, minutes: 0, seconds: 0 });
    const filteredProducts = filterState === 'new-arrivals' ? newProducts : bestSellerProducts;
    const user = JSON.parse(sessionStorage.getItem('user'));

    useEffect(() => {
        const endTime = new Date();
        endTime.setDate(endTime.getDate() + 3);
        endTime.setHours(endTime.getHours() + 1);
        endTime.setMinutes(endTime.getMinutes() + 50);
        endTime.setSeconds(endTime.getSeconds() + 18);

        const interval = setInterval(() => {
            const now = new Date().getTime();
            const distance = endTime - now;

            const days = Math.floor(distance / (1000 * 60 * 60 * 24));
            const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((distance % (1000 * 60)) / 1000);

            setCountdown({ days, hours, minutes, seconds });

            if (distance < 0) {
                clearInterval(interval);
                setCountdown({ days: 0, hours: 0, minutes: 0, seconds: 0 });
            }
        }, 1000);

        return () => clearInterval(interval);
    }, []);


    useEffect(() => {
        const fetchNewProducts = async () => {
            try {
                const response = await listnewProduct();
                setNewProducts(response.data);
            } catch (error) {
                console.error(error);
                setError(error.message);
            }
        };

        fetchNewProducts();
    }, []);

    useEffect(() => {
        const fetchBestSellerProducts = async () => {
            try {
                const response = await listbsProduct();
                setBestSellerProducts(response.data);
            } catch (error) {
                console.error(error);
                setError(error.message);
            }
        };

        fetchBestSellerProducts();
    }, []);

    useEffect(() => {
        // Check for add to cart error
        if (addToCartError) {
            setAddToCartError(null);
        }
    }, [addToCartError]);

    const handleAddToCart = async (itemId) => {
        try {
          await addToCart(user.id, itemId, 1);
          toast.success(`Đã thêm sản phẩm vào giỏ hàng`, {
            position: "top-right",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        } catch (error) {
          console.error('Error add to cart item:', error);
          toast.error(`Lỗi khi thêm sản phẩm vào giỏ hàng`, {
            position: "top-right",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
        }
      };

    return (
        <div>
        <ToastContainer />
            {/* Check for global errors */}
            {error && <div dangerouslySetInnerHTML={{ __html: `swal("Lỗi", "${error}", "error");` }} />}

            {/* Check for add to cart error */}
            {addToCartError && <div dangerouslySetInnerHTML={{ __html: `swal("Lỗi", "${addToCartError}", "error");` }} />}

            <section className="product spad">
                <div className="container">
                    <div className="row">
                        <div className="col-lg-12">
                            <ul className="filter__controls">
                                <li
                                    className={filterState === 'new-arrivals' ? 'active' : ''}
                                    onClick={() => setFilterState('new-arrivals')}
                                >
                                    New Arrivals
                                </li>
                                <li
                                    className={filterState === 'hot-sales' ? 'active' : ''}
                                    onClick={() => setFilterState('hot-sales')}
                                >
                                    Best Sellers
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div className="row product__filter">
                        {filteredProducts.map(product => (
                            <div
                                className="col-lg-3 col-md-6 col-sm-6 col-md-6 col-sm-6 mix new-arrivals"
                                key={product.id}
                            >
                                <div className="product__item">
                                    <div
                                        className="product__item__pic set-bg"
                                        style={{ backgroundImage: `url(${product.productImage[0].url_Image})` }}
                                    >
                                        <span className="label">New</span>
                                        <ul className="product__hover">
                                            <li>
                                                <a href="#">
                                                    <img src="img/icon/heart.png" alt="" />
                                                </a>
                                            </li>
                                            <li>
                                                <a href="#">
                                                    <img src="img/icon/compare.png" alt="" />
                                                    <span>Compare</span>
                                                </a>
                                            </li>
                                            <li>
                                                <a href={`/productdetail/${product.id}`}>
                                                    <img src="img/icon/search.png" alt="" />
                                                </a>
                                            </li>
                                        </ul>
                                    </div>
                                    <div className="product__item__text">
                                        <h6>{product.product_Name}</h6>
                                        <a
                                            onClick={() => handleAddToCart(product.id)}
                                            style={{ cursor: 'pointer' }}
                                             className="add-cart text-danger"
                                                >
                                            + Add To Cart
                                         </a>
                                        <div className="rating">
                                            <i className="fa fa-star-o" />
                                            <i className="fa fa-star-o" />
                                            <i className="fa fa-star-o" />
                                            <i className="fa fa-star-o" />
                                            <i className="fa fa-star-o" />
                                        </div>
                                        <h5>{`${new Intl.NumberFormat('vi-VN').format(product.price)} VNĐ`}</h5>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </section>

            <section className="categories spad">
            <div className="container">
                <div className="row">
                    <div className="col-lg-3">
                        <div className="categories__text">
                            <h2>
                                Clothings Hot <br /> <span>Shoe Collection</span> <br />
                                Accessories
                            </h2>
                        </div>
                    </div>
                    <div className="col-lg-4">
                        <div className="categories__hot__deal">
                            <img src="img/product-sale.png" alt="" />
                            <div className="hot__deal__sticker">
                                <span>Sale Of</span>
                                <h5>100.000đ</h5>
                            </div>
                        </div>
                    </div>
                    <div className="col-lg-4 offset-lg-1">
                        <div className="categories__deal__countdown">
                            <span>Deal Of The Week</span>
                            <h2>Multi-pocket Chest Bag Black</h2>
                            <div className="categories__deal__countdown__timer" id="countdown">
                                <div className="cd-item">
                                    <span>{countdown.days}</span>
                                    <p>Days</p>
                                </div>
                                <div className="cd-item">
                                    <span>{countdown.hours}</span>
                                    <p>Hours</p>
                                </div>
                                <div className="cd-item">
                                    <span>{countdown.minutes}</span>
                                    <p>Minutes</p>
                                </div>
                                <div className="cd-item">
                                    <span>{countdown.seconds}</span>
                                    <p>Seconds</p>
                                </div>
                            </div>
                            <a href="#" className="primary-btn">Shop now</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section className="instagram spad">
            <div className="container">
                <div className="row">
                    <div className="col-lg-8">
                        <div className="instagram__pic">
                            <div className="instagram__pic__item set-bg"
                                style={{backgroundImage: 'url(img/instagram/instagram-1.jpg)'}}></div>
                            <div className="instagram__pic__item set-bg"
                                style={{backgroundImage: 'url(img/instagram/instagram-2.jpg)'}}></div>
                            <div className="instagram__pic__item set-bg"
                                style={{backgroundImage: 'url(img/instagram/instagram-3.jpg)'}}></div>
                            <div className="instagram__pic__item set-bg"
                                style={{backgroundImage: 'url(img/instagram/instagram-4.jpg)'}}></div>
                            <div className="instagram__pic__item set-bg"
                                style={{backgroundImage: 'url(img/instagram/instagram-5.jpg)'}}></div>
                            <div className="instagram__pic__item set-bg"
                                style={{backgroundImage: 'url(img/instagram/instagram-6.jpg)'}}></div>
                        </div>
                    </div>
                    <div className="col-lg-4">
                        <div className="instagram__text">
                            <h2>Instagram</h2>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                                sed do eiusmod tempor incididunt ut labore et dolore magna
                                aliqua.</p>
                            <h3>#Male_Fashion</h3>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section className="latest spad">
            <div className="container">
                <div className="row">
                    <div className="col-lg-12">
                        <div className="section-title">
                            <span>Latest News</span>
                            <h2>Fashion New Trends</h2>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-lg-4 col-md-6 col-sm-6">
                        <div className="blog__item">
                            <div className="blog__item__pic set-bg"
                                style={{backgroundImage: 'url(img/blog/blog-1.jpg)'}}></div>
                            <div className="blog__item__text">
                                <span><img src="img/icon/calendar.png" alt=""/> 16
                                    February 2020</span>
                                <h5>What Curling Irons Are The Best Ones</h5>
                                <a href="#">Read More</a>
                            </div>
                        </div>
                    </div>
                    <div className="col-lg-4 col-md-6 col-sm-6">
                        <div className="blog__item">
                            <div className="blog__item__pic set-bg"
                                style={{backgroundImage: 'url(img/blog/blog-2.jpg)'}}></div>
                            <div className="blog__item__text">
                                <span><img src="img/icon/calendar.png" alt=""/> 21
                                    February 2020</span>
                                <h5>Eternity Bands Do Last Forever</h5>
                                <a href="#">Read More</a>
                            </div>
                        </div>
                    </div>
                    <div className="col-lg-4 col-md-6 col-sm-6">
                        <div className="blog__item">
                            <div className="blog__item__pic set-bg"
                                style={{backgroundImage: 'url(img/blog/blog-3.jpg)'}}></div>
                            <div className="blog__item__text">
                                <span><img src="img/icon/calendar.png" alt=""/> 28
                                    February 2020</span>
                                <h5>The Health Benefits Of Sunglasses</h5>
                                <a href="#">Read More</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        </div>
    );
};

export default Home;