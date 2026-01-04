import bikeImg from "../assets/images/bike.jpeg"
import { generatePriceRanges } from "@/utils";

const products = Array(15).fill({
  category: "Mountain bikes",
  img: bikeImg,
  productName: "S-WORKS Enduro",
  owner: { url: "/", name: "Sigma" },
  desc: "Lorem ipsum dolor sit amet consectetur. Et nisl elementum sagittis aliquet enim scelerisque elit.",
  price: 300,
  productUrl: "/"
})

const categories = [
  { id: 1, name: "snowboarding gear" },
  { id: 2, name: "skiing gear" },
  { id: 3, name: "surfing gear" },
  { id: 4, name: "cycling gear" },
  { id: 5, name: "hiking gear" },
  { id: 6, name: "climbing gear" },
  { id: 7, name: "winter sports" },
  { id: 8, name: "water sports" },
  { id: 9, name: "fitness equipment" },
  { id: 10, name: "team sports" }
];

const priceRanges: { start: number; end?: number }[] = [...generatePriceRanges(5, 20, 0), ...generatePriceRanges(1, 50, 1), { start: 150 }]

const subscriptionBenefits = ["Unlimited Listings", "Damage Reporting Tools", "Secure Payments", "Deposit Management"]

const reservations = [
  {
    itemName: "Teniski reket",
    traderName: "Ivan Horvat",
    status: "active",
    dateStart: "12.3.2025",
    dateEnd: "19.3.2025"
  },
  {
    itemName: "Nogometna lopta",
    traderName: "Marko Kovač",
    status: "inactive",
    dateStart: "5.1.2025",
    dateEnd: "12.1.2025"
  },
  {
    itemName: "Skijaške pancerice",
    traderName: "Ana Marić",
    status: "active",
    dateStart: "20.2.2025",
    dateEnd: "27.2.2025"
  },
  {
    itemName: "Bicikl",
    traderName: "Petar Novak",
    status: "inactive",
    dateStart: "1.4.2025",
    dateEnd: "10.4.2025"
  },
  {
    itemName: "Fitness podloga",
    traderName: "Lucija Barišić",
    status: "active",
    dateStart: "15.3.2025",
    dateEnd: "22.3.2025"
  },
  {
    itemName: "Košarkaška lopta",
    traderName: "Tomislav Jurić",
    status: "inactive",
    dateStart: "10.2.2025",
    dateEnd: "17.2.2025"
  },
  {
    itemName: "Rolice",
    traderName: "Maja Babić",
    status: "active",
    dateStart: "3.5.2025",
    dateEnd: "10.5.2025"
  },
  {
    itemName: "Kaciga za bicikl",
    traderName: "Nikola Radić",
    status: "inactive",
    dateStart: "18.1.2025",
    dateEnd: "25.1.2025"
  },
  {
    itemName: "Pojas za vježbanje",
    traderName: "Katarina Šimić",
    status: "active",
    dateStart: "8.3.2025",
    dateEnd: "15.3.2025"
  },
  {
    itemName: "Skateboard",
    traderName: "Dario Perić",
    status: "inactive",
    dateStart: "22.4.2025",
    dateEnd: "29.4.2025"
  }
];

export {
  products,
  categories,
  priceRanges,
  subscriptionBenefits,
  reservations
}
