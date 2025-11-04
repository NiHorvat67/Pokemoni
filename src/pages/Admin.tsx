import Button from "@/components/Button";
import Input from "@/components/Input";
import { useState } from "react";
import { dateRangeIcon } from "@/assets/icons";

const Admin = () => {

  const [price, setPrice] = useState("")

  return (
    <section className="padding-x padding-t">
      <div className="font-inter max-container flex flex-col gap-18 sm:gap-20">

        <h1 className="font-medium text-2xl text-white">Admin panel</h1>

        <section className="">
          <h2 className="text-white text-xl font-medium mb-3">Yearly subscription — 5€</h2>
          <p className="text-desc mb-7">Set the price of the yearly subscription</p>
          <form className="flex gap-4 sm:gap-7">
            <Input placeholder="Price" state={price} setState={setPrice} />
            <Button text="Update" icon={false} long={false} onClick={() => { }} />
          </form>

        </section>

        <section>
          <h2 className="text-white text-xl font-medium mb-3">Reports</h2>
          <p className="text-desc mb-7">Displaying all customer-related reports from sellers.</p>

          <div className="flex flex-col gap-8">

            <div className="flex gap-6 flex-col bg-[#222423] rounded-[8px] sm:px-7 py-4 px-5 sm:py-5 sm:px-9 sm:py-7 max-w-[670px]">
              <div className="flex flex-wrap gap-5 justify-between text-white">
                <h2 className="">
                  <a href="/" className="font-bold">Craig George </a>
                  reported
                  <a href="/" className="font-bold"> Jaydon Vetrovs</a>
                </h2>
                <div className="flex items-center gap-2 font-medium">
                  <img src={dateRangeIcon} alt="date icon" />
                  24.10.2025
                </div>
              </div>

              <p className="text-desc">
                The equipment was returned several days after the agreed date, causing scheduling issues for other customers who had already made reservations.
              </p>

              <div className="flex gap-5 sm:gap-7 flex-wrap">
                <Button icon={false} text="Block account" className="!bg-[#E5002E] hover:shadow-[0_0_15px_#E5002E] text-white !py-2 !px-4" long={false} onClick={() => { }} />
                <Button icon={false} text="Disable renting" className="!bg-[#EED202] hover:shadow-[0_0_15px_#EED202] text-black !py-2 !px-4" long={false} onClick={() => { }} />
              </div>
            </div>


          </div>
        </section>


      </div>
    </section>
  );
}

export default Admin;
