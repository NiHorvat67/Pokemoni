


const RateService = ({ rating, setRating }: { rating: any, setRating: any }) => {

  const ratings = [1, 2, 3, 4, 5]

  return (
    <>
      <div className="flex gap-4">
        {
          ratings.map((ratingNumber) => (
            <span
              className={`selection:bg-transparent text-[16px] w-11 h-11 font-regular rounded-full transition duration-200 ease-in-out ${rating === ratingNumber ? "bg-dark-bg text-white" : "bg-neutral-200/70"} hover:bg-dark-bg hover:text-white cursor-pointer flex items-center justify-center`}
              onClick={() => setRating(ratingNumber)}
            >
              {ratingNumber}
            </span>
          ))
        }
      </div >
    </>
  );
}

export default RateService;
