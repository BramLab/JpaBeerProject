findAllBrewersWithBeerCount

select br.id, br.name, br.location, count(*)
    from brewer br
    left join belgium_beerdb.beer be on br.id = be.brewer_id
group by br.id, br.location, br.name;