import profileImg from "../assets/images/profile.jpeg"
import { locationIcon, mailIcon, starIcon } from "../assets/icons"
import "../styles/TraderProfile.css"
import ProductCard from "../components/ProductCard"
import bikeImg from "../assets/images/bike.jpeg"

const TraderProfile = () => {

  const infoItems = [
    { icon: mailIcon, text: "craigjones@gmail.com" },
    { icon: locationIcon, text: "Random Adresa 21, Zagreb 10000, Hrvatska" },
    { icon: starIcon, text: "4.5/5" },
  ]

  const products = Array(5).fill({
    category: "Mountain bikes",
    img: bikeImg,
    productName: "S-WORKS Enduro",
    owner: { url: "/", name: "Sigma" },
    desc: "Lorem ipsum dolor sit amet consectetur. Et nisl elementum sagittis aliquet enim scelerisque elit.",
    price: 300,
    productUrl: "/"
  })

  return (
    <section className="padding-x pt-30 sm:pt-45 pb-20">
      <section className="max-container">

        <section id="info-container" className="mb-19 sm:mb-34">
          <img
            id="profile-image"
            src={profileImg}
            alt="profile"
            className="sm:ml-[-10px] ml-[-4px] rounded-full h-[152px] w-[152px] sm:h-[206px] sm:w-[206px] object-cover"
          />
          <div id="name">
            <h1 className="text-white text-2xl sm:text-[32px] font-medium mb-1">Craig George</h1>
            <p className="font-inter text-[#C4CCC7]">Trgovac</p>
          </div>

          <div id="desc" className="flex gap-4 flex-col">
            {infoItems.map((item) => (
              <div key={item.text} className="text-white font-inter flex items-start gap-3">
                <img src={item.icon} alt="list item icon" />
                {item.text}
              </div>
            ))}
          </div>


        </section>

        <h1 className="text-white font-inter font-medium text-xl sm:text-2xl mb-[54px]">Craig George is renting</h1>

        {/* <section className="flex gap-11 flex-wrap"> */}
        <section className="grid min-[1250px]:grid-cols-4 min-[1000px]:grid-cols-3 min-[700px]:grid-cols-2 grid-cols-1 max-sm:justify-items-center gap-11">
          {products.map((product, index) => (
            <ProductCard key={index}
              category={product.category}
              img={product.img}
              productName={product.productName}
              owner={product.owner}
              desc={product.desc}
              price={product.price}
              productUrl={product.productUrl}
            ></ProductCard>
          ))}
        </section>

      </section>
    </section>
  );
}


export default TraderProfile;
