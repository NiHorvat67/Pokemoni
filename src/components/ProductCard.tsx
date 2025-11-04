import bikeImg from "../assets/images/bike.jpeg"


const ProductCard = ({ category, img, productName, owner, desc, price, advertisementId }:
  {
    category: string,
    productName: string,
    owner: { name: string, id: number },
    desc: string,
    price: number,
    img: any,
    advertisementId: number
  }) => {


  return (
    <article className="relative max-w-[320px] font-inter">
      <div className="absolute top-[12px] left-[24px] text-primary bg-[#102B19] rounded-[8px] px-2.5 py-1.25 text-[13px] font-inter">
        {category}
      </div>
      <img src={bikeImg} alt="product image" className="w-full h-[225px] object-cover rounded-t-[8px]" />

      <div className="bg-[#222423] rounded-[8px] p-6 mt-[-8px] relative shadow-[0_-4px_4px_rgba(0,0,0,0.25)]">
        <h1 className="font-medium text-[20px] text-white mb-2 line-clamp-1">{productName}</h1>
        <p className="text-[#D2D9D4] text-[14px] mb-7.5 line-clamp-1">
          by
          <a className="font-medium" href={`/profile/${owner.id}`}> {owner.name}</a>
        </p>
        <div className="text-[#D2D9D4] text-[14px] mb-7.5 xh-[2lh] line-clamp-2 h-[2lh]">{desc}</div>
        <div className="flex items-center justify-between">
          <span className="text-white font-semibold text-2xl">{price}€</span>
          <a className="text-black px-4 py-1.5 sm:px-5 sm:py-2 bg-primary rounded-[8px] hover:shadow-[0_0_15px_#00FA55] transition-shadow duration-300" href={`/advertisement/${advertisementId}`}>Rent</a>
        </div>
      </div>

    </article>
  );
}

export default ProductCard;
