import heroImg from "../assets/images/hero.jpg"
import Search from "../components/Search";
import ProductCard from "../components/ProductCard";
import bikeImg from "../assets/images/bike.jpeg"
import Calendar23 from "@/components/calendar-23";

import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"


const Homepage = () => {

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

    <section className="padding-x pt-30 sm:pt-50">
      <div className="h-[560px] w-[100vw] absolute top-0 left-0 right-0">
        <img src={heroImg} alt="" className="h-full w-full object-cover object-center" />
        <div className="w-full h-[100.1%] absolute top-0 bg-gradient-to-b from-[#121413]/[0%] from-70% to-dark-bg to-100%"></div>
      </div>

      <section className="max-container relative pt-77">
        <form action="" className="flex flex-col items-center mb-25 sm:mb-40 gap-4 sm:gap-6">
          <Search name="query" placeholder="Search gear" />
          <div className="flex flex-wrap items-center justify-center gap-4 sm:gap-6 font-inter">
            <Select>
              <SelectTrigger className="w-[130px] sm:w-[180px]">
                <SelectValue placeholder="Category" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="light">Light</SelectItem>
                <SelectItem value="dark">Dark</SelectItem>
                <SelectItem value="system">System</SelectItem>
              </SelectContent>
            </Select>

            <Calendar23></Calendar23>

            <Select>
              <SelectTrigger className="w-[130px] sm:w-[180px]">
                <SelectValue placeholder="Price" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="light">Light</SelectItem>
                <SelectItem value="dark">Dark</SelectItem>
                <SelectItem value="system">System</SelectItem>
              </SelectContent>
            </Select>
          </div>


        </form>

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

export default Homepage;
