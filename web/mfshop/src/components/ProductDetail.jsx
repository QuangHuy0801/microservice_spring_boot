import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { productdetail } from "../services/ProductService";
import { addToCart } from "../services/CartService";
import { ToastContainer, toast } from "react-toastify";

const ProductDetail = () => {
	const { id } = useParams();
	const [quantity, setQuantity] = useState(1);
	const [product, setProduct] = useState(null);
	const [activeTab, setActiveTab] = useState(0);
	const [relatedProducts, setRelatedProducts] = useState([]);
	const [loading, setLoading] = useState(true); // Add a loading state
	const [error, setError] = useState(null); // Add an error state
	const user = JSON.parse(sessionStorage.getItem('user'));

	const handleTabClick = (index) => {
		setActiveTab(index);
	};

    const handleAddToCart = async (product_id,count) => {
		try {
		await addToCart(user.id, product_id,count);
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
	
	useEffect(() => {
		const fetchProductDetail = async () => {
			try {
				const response = await productdetail(id);
				console.log('API response:', response.data);
				setProduct(response.data.product);
				setRelatedProducts(response.data.relatedProducts);
				setLoading(false); // Set loading to false after data is fetched
			} catch (error) {
				console.error("Error fetching product details:", error);
				setError(error);
				setLoading(false); // Set loading to false even if there's an error
			}
		};

		fetchProductDetail();
	}, []);

	// if (!product) {
	// 	return <div>Loading...</div>;
	// }
	if (loading) {
		return <div>Loading...</div>;
	}

	if (error) {
		return <div>Error loading product details: {error.message}</div>;
	}

	if (!product) {
		return <div>Product not found</div>;
	}

	return (
		<div>
		<ToastContainer />
			<section className="shop-details">
				<div className="product__details__pic">
					<div className="container">
						<div className="row">
							<div className="col-lg-12">
								<div className="product__details__breadcrumb">
									<a className="ml-5" href="/home">
										Home
									</a>
									<a className="ml-5" href="/shop">
										Shop
									</a>
									<span className="ml-5">Product Details</span>
								</div>
							</div>
						</div>
						<div className="row">
							<div className="col-lg-3 col-md-3">
								<ul className="nav nav-tabs" role="tablist">
									{product.productImage.map((image, index) => (
										<li key={index} className="nav-item">
											<a
												className={`nav-link ${activeTab === index ? "active" : ""
													}`}
												onClick={() => handleTabClick(index)}
												role="tab"
											>
												<div
													className="product__thumb__pic set-bg"
													style={{ backgroundImage: `url(${image.url_Image})` }}
												></div>
											</a>
										</li>
									))}
								</ul>
							</div>
							<div className="col-lg-6 col-md-9">
								<div className="tab-content">
									{product.productImage.map((image, index) => (
										<div
											key={index}
											className={`tab-pane ${activeTab === index ? "active" : ""
												}`}
											role="tabpanel"
										>
											<div className="product__details__pic__item">
												<img src={image.url_Image} alt="" />
											</div>
										</div>
									))}
								</div>
							</div>
						</div>
					</div>
				</div>
				<div className="product__details__content">
					<div className="container">
						<div className="row d-flex justify-content-center">
							<div className="col-lg-8">
								<div className="product__details__text">
									<h4>{product.product_Name}</h4>
									<div className="rating">
										<i className="fa fa-star"></i>{" "}
										<i className="fa fa-star"></i>{" "}
										<i className="fa fa-star"></i>{" "}
										<i className="fa fa-star"></i>{" "}
										<i className="fa fa-star-o"></i> <span> - 5 Reviews</span>
									</div>
									<h3>{product.price} VNĐ</h3>
									<p>{product.description}</p>

										<div className="product__details__cart__option">
											<div className="quantity">
												<div className="pro-qty">
													<input
														name="count"
														type="text"
														value={quantity}
														onChange={(e) => setQuantity(e.target.value)}
													/>
												</div>
												<input
													name="product_id"
													type="hidden"
													value={product.id}
												/>
											</div>
											<button className="primary-btn" onClick={() => handleAddToCart(id, quantity)}>Add to Cart</button>
										</div>

									<div className="product__details__last__option">
										<h5>
											<span>Guaranteed Safe Checkout</span>
										</h5>
										<img src="src/img/shop-details/details-payment.png" alt="" />
										<ul>
											<li>
												<span>SKU:</span> 3812912
											</li>
											<li>
												<span>Categories:</span> Clothes
											</li>
											<li>
												<span>Tag:</span> Clothes, Skin, Body
											</li>
										</ul>
									</div>
								</div>
							</div>
						</div>
						<div className="row">
							<div className="col-lg-12">
								<div className="product__details__tab">
									<ul className="nav nav-tabs" role="tablist">
										<li className="nav-item">
											<a
												className="nav-link active"
												data-toggle="tab"
												href="#tabs-5"
												role="tab"
											>
												Description
											</a>
										</li>
										<li className="nav-item">
											<a
												className="nav-link"
												data-toggle="tab"
												href="#tabs-6"
												role="tab"
											>
												Customer Previews(5)
											</a>
										</li>
										<li className="nav-item">
											<a
												className="nav-link"
												data-toggle="tab"
												href="#tabs-7"
												role="tab"
											>
												Additional information
											</a>
										</li>
									</ul>
									<div className="tab-content">
										<div
											className="tab-pane active"
											id="tabs-5"
											role="tabpanel"
										>
											<div className="product__details__tab__content">
												<p className="note">
													Nam tempus turpis at metus scelerisque placerat nulla
													deumantos solicitud felis. Pellentesque diam dolor,
													elementum etos lobortis des mollis ut risus. Sedcus
													faucibus an sullamcorper mattis drostique des commodo
													pharetras loremos.
												</p>
												<div className="product__details__tab__content__item">
													<h5>Products Infomation</h5>
													<p>
														A Pocket PC is a handheld computer, which features
														many of the same capabilities as a modern PC. These
														handy little devices allow individuals to retrieve
														and store e-mail messages, create a contact file,
														coordinate appointments, surf the internet, exchange
														text messages and more. Every product that is
														labeled as a Pocket PC must be accompanied with
														specific software to operate the unit and must
														feature a touchscreen and touchpad.
													</p>
													<p>
														As is the case with any new technology product, the
														cost of a Pocket PC was substantial during it’s
														early release. For approximately $700.00, consumers
														could purchase one of top-of-the-line Pocket PCs in
														2003. These days, customers are finding that prices
														have become much more reasonable now that the
														newness is wearing off. For approximately $350.00, a
														new Pocket PC can now be purchased.
													</p>
												</div>
												<div className="product__details__tab__content__item">
													<h5>Material used</h5>
													<p>
														Polyester is deemed lower quality due to its none
														natural qualities. Made from synthetic materials,
														not natural like wool. Polyester suits become
														creased easily and are known for not being
														breathable. Polyester suits tend to have a shine to
														them compared to wool and cotton suits, this can
														make the suit look cheap. The texture of velvet is
														luxurious and breathable. Velvet is a great choice
														for dinner party jacket and can be worn all year
														round.
													</p>
												</div>
											</div>
										</div>
										<div className="tab-pane" id="tabs-6" role="tabpanel">
											<div className="product__details__tab__content">
												<div className="product__details__tab__content__item">
													<h5>Products Information</h5>
													<p>
														A Pocket PC is a handheld computer, which features
														many of the same capabilities as a modern PC. These
														handy little devices allow individuals to retrieve
														and store e-mail messages, create a contact file,
														coordinate appointments, surf the internet, exchange
														text messages and more. Every product that is
														labeled as a Pocket PC must be accompanied with
														specific software to operate the unit and must
														feature a touchscreen and touchpad.
													</p>
													<p>
														As is the case with any new technology product, the
														cost of a Pocket PC was substantial during it’s
														early release. For approximately $700.00, consumers
														could purchase one of top-of-the-line Pocket PCs in
														2003. These days, customers are finding that prices
														have become much more reasonable now that the
														newness is wearing off. For approximately $350.00, a
														new Pocket PC can now be purchased.
													</p>
												</div>
												<div className="product__details__tab__content__item">
													<h5>Material used</h5>
													<p>
														Polyester is deemed lower quality due to its none
														natural qualities. Made from synthetic materials,
														not natural like wool. Polyester suits become
														creased easily and are known for not being
														breathable. Polyester suits tend to have a shine to
														them compared to wool and cotton suits, this can
														make the suit look cheap. The texture of velvet is
														luxurious and breathable. Velvet is a great choice
														for dinner party jacket and can be worn all year
														round.
													</p>
												</div>
											</div>
										</div>
										<div className="tab-pane" id="tabs-7" role="tabpanel">
											<div className="product__details__tab__content">
												<p className="note">
													Nam tempus turpis at metus scelerisque placerat nulla
													deumantos solicitud felis. Pellentesque diam dolor,
													elementum etos lobortis des mollis ut risus. Sedcus
													faucibus an sullamcorper mattis drostique des commodo
													pharetras loremos.
												</p>
												<div class="product__details__tab__content__item">
													<h5>Products Infomation</h5>
													<p>
														A Pocket PC is a handheld computer, which features
														many of the same capabilities as a modern PC. These
														handy little devices allow individuals to retrieve
														and store e-mail messages, create a contact file,
														coordinate appointments, surf the internet, exchange
														text messages and more. Every product that is
														labeled as a Pocket PC must be accompanied with
														specific software to operate the unit and must
														feature a touchscreen and touchpad.
													</p>
													<p>
														As is the case with any new technology product, the
														cost of a Pocket PC was substantial during it’s
														early release. For approximately $700.00, consumers
														could purchase one of top-of-the-line Pocket PCs in
														2003. These days, customers are finding that prices
														have become much more reasonable now that the
														newness is wearing off. For approximately $350.00, a
														new Pocket PC can now be purchased.
													</p>
												</div>
												<div class="product__details__tab__content__item">
													<h5>Material used</h5>
													<p>
														Polyester is deemed lower quality due to its none
														natural quality’s. Made from synthetic materials,
														not natural like wool. Polyester suits become
														creased easily and are known for not being
														breathable. Polyester suits tend to have a shine to
														them compared to wool and cotton suits, this can
														make the suit look cheap. The texture of velvet is
														luxurious and breathable. Velvet is a great choice
														for dinner party jacket and can be worn all year
														round.
													</p>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
			<section className="related spad">
				<div className="container">
					<div className="row">
						<div className="col-lg-12">
							<h3 className="related-title">Related Product</h3>
						</div>
					</div>
					<div className="row">
						{relatedProducts.map((product, index) => (
							<div className="col-lg-3 col-md-6 col-sm-6" key={index}>
								<div className="product__item">
									<div className="product__item__pic set-bg" style={{ backgroundImage: `url(${product.productImage[0].url_Image})` }}>
										<span className="label">New</span>
										<ul className="product__hover">
											<li><a href="#"><img src="/img/icon/heart.png" alt="" /></a></li>
											<li><a href="#"><img src="/img/icon/compare.png" alt="" /> <span>Compare</span></a></li>
											<li><a href={`/productDetail/${product.id}`}><img src="/img/icon/search.png" alt="" /></a></li>
										</ul>
									</div>
									<div className="product__item__text">
										<h6>{product.product_Name}</h6>
										<a
                          onClick={() => handleAddToCart(product.id,1)}
                          style={{ cursor: 'pointer' }}
                          className="add-cart text-danger"
                        >
                          + Add To Cart
                        </a>
										<div className="rating">
											<i className="fa fa-star-o"></i>
											<i className="fa fa-star-o"></i>
											<i className="fa fa-star-o"></i>
											<i className="fa fa-star-o"></i>
											<i className="fa fa-star-o"></i>
										</div>
										<h5>{new Intl.NumberFormat('en-US').format(product.price)} VNĐ</h5>
									</div>
								</div>
							</div>
						))}
					</div>
				</div>
			</section>
		</div>
	);
};

export default ProductDetail;

   